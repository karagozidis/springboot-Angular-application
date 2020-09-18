package crm.cloudApp.backend.services.data.modules.market;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.models.data.modules.market.BMECommand;
import crm.cloudApp.backend.repositories.data.modules.market.BMECommandRepository;
import crm.cloudApp.backend.services.users.UserService;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Data
@Component
public class TradingProcessInteractionService {

  private Map<String, BufferedWriter> writers;
  private Map<String, BufferedReader> readers;
  private Map<String, Process> processes;

  @Value("${modules.market.scrypt}")
  private String scrypt;

  @Value("${modules.market.scryptPath}")
  private String scryptPath;

  @Value("${modules.market.storeBMECommands}")
  private Boolean storeBMECommands;

  private final BMECommandRepository bmeCommandRepository;
  private final UserService userService;
  private final SimpMessagingTemplate simpMessagingTemplate;
  private Boolean logBMECommandsToWebSockets = false;

  public TradingProcessInteractionService(BMECommandRepository bmeCommandRepository,
      UserService userService,
      SimpMessagingTemplate simpMessagingTemplate) {
    this.bmeCommandRepository = bmeCommandRepository;
    this.userService = userService;
    this.simpMessagingTemplate = simpMessagingTemplate;
  }

  public void setLogBMECommandsToWebSockets(Boolean logBMECommandsToWebSockets) {
    this.logBMECommandsToWebSockets = logBMECommandsToWebSockets;
  }

  public Boolean getLogBMECommandsToWebSockets() {
    return logBMECommandsToWebSockets;
  }


  @PostConstruct
  public void init() {
    this.writers = new HashMap<>();
    this.readers = new HashMap<>();
    this.processes = new HashMap<>();

    this.init("BG");
    this.init("CY");
  }

  public void init(String marketCode) {
    try {
      ProcessBuilder processBuilder = new ProcessBuilder();
      processBuilder.directory(new File(scryptPath));
      processBuilder.command(scrypt, "eurodyn");
      processBuilder.redirectErrorStream(true);
      Process process = processBuilder.start();

      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      this.writers.put(marketCode, writer);
      this.readers.put(marketCode, reader);
      this.processes.put(marketCode, process);

    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public synchronized List<String> marketInteraction(List<BMECommand> commands,
      Boolean isBasketCreation, String marketCode) throws IOException {

    if (isBasketCreation) {
      return runBasketCreationCommandsSequence(commands, marketCode);
    } else {
      return runCommandsSequence(commands, marketCode);
    }
  }

  private List<String> runCommandsSequence(List<BMECommand> commands, String marketCode)
      throws IOException {
    for (BMECommand command : commands) {
      this.write(command, marketCode);
    }
    return this.read(marketCode);
  }

  private List<String> runBasketCreationCommandsSequence(List<BMECommand> commands,
      String marketCode) throws IOException {

    this.write(commands.get(0), marketCode);
    List<String> messages = this.read(marketCode);

    String basketId = "";
    for (String message : messages) {
      String[] messageParts = message.split(" ");
      basketId = messageParts[1];
    }

    BMECommand basketSelectionCommnd = commands.get(1);
    String updatedMessage = basketSelectionCommnd.getMessage().replace("{basket_id}", basketId);
    basketSelectionCommnd.setMessage(updatedMessage);
    this.write(basketSelectionCommnd, marketCode);
    messages = this.read(marketCode);

    for (int i = 2; i < commands.size(); i++) {
      this.write(commands.get(i), marketCode);
      messages = this.read(marketCode);
    }

    return messages;
  }

  private void write(BMECommand command, String marketCode) throws IOException {
    if (storeBMECommands) {
      this.storeBMECommand(command);
    }
    writers.get(marketCode).write(command.getMessage());
    log.info(command.getMessage());
    this.logBMECommandsToWebSockets("-->", command.getMessage());
    writers.get(marketCode).newLine();
    writers.get(marketCode).flush();
  }


  void logBMECommandsToWebSockets(String direction, String message) {
    if (logBMECommandsToWebSockets) {
      this.simpMessagingTemplate.convertAndSend("/topic/market/bme-commands",
          direction + " " + message);
    }
  }

  private List<String> read(String marketCode) throws IOException {

    String message = "";
    List<BMECommand> bmeCommands = new ArrayList<>();

    message = readers.get(marketCode).readLine();
    bmeCommands.add(new BMECommand(message, AppConstants.Types.CommandType.INPUT));
    log.info(message);
    this.logBMECommandsToWebSockets("<--", message);
    while (readers.get(marketCode).ready()
        && (message = readers.get(marketCode).readLine()) != null) {
      bmeCommands.add(new BMECommand(message, AppConstants.Types.CommandType.INPUT));
      log.info(message);
      this.logBMECommandsToWebSockets("<--", message);
    }

    if (storeBMECommands) {
      this.storeBMECommands(bmeCommands);
    }

    return bmeCommands.stream()
        .map(BMECommand::getMessage).collect(Collectors.toList());
  }

  private void storeBMECommands(List<BMECommand> commands) {
    for (BMECommand command : commands) {
      this.storeBMECommand(command);
    }
  }

  private void storeBMECommand(BMECommand command) {
    command.setCreatedBy(userService.getLoggedInUserName());
    command.setCreatedOn(Instant.now());
    command.setModifiedBy(userService.getLoggedInUserName());
    command.setModifiedOn(Instant.now());
    bmeCommandRepository.save(command);
  }


  public Boolean isProcessAlive(String marketCode) {
    return processes.get(marketCode).isAlive();
  }

  public void resetMarketProcess(String marketCode) {
    if (this.writers.containsKey(marketCode)) {
      this.writers.remove(marketCode);
    }
    if (this.readers.containsKey(marketCode)) {
      this.readers.remove(marketCode);
    }
    if (this.processes.containsKey(marketCode)) {
      this.processes.remove(marketCode);
    }

    this.init(marketCode);
  }
}
