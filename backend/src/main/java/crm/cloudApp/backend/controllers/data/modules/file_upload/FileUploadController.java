package crm.cloudApp.backend.controllers.data.modules.file_upload;

import crm.cloudApp.backend.dto.data.MessageInfoDTO;
import crm.cloudApp.backend.services.data.modules.file_upload.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/modules/file-uploader")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(value = "/weather")
    @ResponseStatus(HttpStatus.OK)
    public void uploadWeather(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        this.fileUploadService.uploadFiles(multipartFile, "weather_upload");
    }

    @PostMapping(value = "/files")
    @ResponseStatus(HttpStatus.OK)
    public void uploadFiles(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("user-group") Long userGroup
    ) throws IOException {

        if (userGroup.equals(0L)) this.fileUploadService.uploadFiles(multipartFile, "file_upload");
        else this.fileUploadService.uploadFiles(multipartFile, "file_upload", userGroup);
    }

    @PostMapping(value = "/files-by-tag")
    @ResponseStatus(HttpStatus.OK)
    public void uploadFilesByTag(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("user-group") Long userGroup,
            @RequestParam("tag") String tag
    ) throws IOException {

        if (userGroup.equals(0L)) this.fileUploadService.uploadFiles(multipartFile, "file_upload");
        else this.fileUploadService.uploadFiles(multipartFile, tag, userGroup);
    }


    @PostMapping(value = "/notification-for-file")
    public Boolean createNotifications(
            @RequestParam("message-info-id") Long messageInfoId
    ) {
        this.fileUploadService.createNotifications(messageInfoId);
        return true;
    }


    @GetMapping
    public List<MessageInfoDTO> getAll() {
        return this.fileUploadService.getAll();
    }


    @GetMapping(value = "/by-tags")
    public List<MessageInfoDTO> getAll( @RequestParam("tags") List<String> tags) {
        return this.fileUploadService.getByTags(tags);
    }


    @GetMapping("/download")
    public void downloadFile(@RequestParam("id") Long id,
                             HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=download.csv");
        response.setStatus(HttpServletResponse.SC_OK);

        this.fileUploadService.downloadFile(response, id);
    }

    @DeleteMapping
    public void delete(@RequestParam("id") Long id) {
        this.fileUploadService.delete(id);
    }

}
