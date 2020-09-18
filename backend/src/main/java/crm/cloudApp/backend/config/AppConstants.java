package crm.cloudApp.backend.config;

public class AppConstants {

  public class Jwt {

    // The unique JWT id.
    public static final String JWT_CLAIM_ID = "jti";
    // The time in seconds ago this JWT was generated.
    public static final String JWT_CLAIM_CREATED_AT = "iat";
    public static final String JWT_CLAIM_EMAIL = "sub";
    public static final String JWT_CLAIM_ISSUER = "iss";
    public static final String JWT_CLAIM_EXPIRES_AT = "exp";
    public static final String JWT_CLAIM_USER_ID = "user_id";
    public static final String JWT_CLAIM_SESSION_ID = "session_id";
  }

  public static class Types {

    public enum UserStatus {
      enabled,
      disabled,
      deleted
    }

    public enum ContactStatus {
      viewed,
      pending,
      rejected,
      accepted
    }

    public enum UserGroupStatus {
      enabled,
      deleted
    }

    public enum NotificationStatus {
      pending,
      viewed
    }

    public enum NotificationType {
      contactEstablishment,
      userGroupAssignment,
      marketTradeCreation
    }

    public enum Status {
      enabled,
      disabled
    }

    public enum ExecStatus {
      NotStarted,
      Running,
      Executed,
      Failed
    }

    public enum OrderDirection {
      SELL,
      BUY
    }

    public enum SingleOrderType {
      ICEBERG,
      IMMEDIATE_OR_CANCEL,
      FILL_OR_KILL,
      ALL_OR_NONE,
      LIMIT,
      BASKET
    }

    public enum BasketStatus {
      ACTIVE,
      DEACTIVATED
    }

    public enum SingleOrderStatus {
      ACTIVE,
      PARTLY_MATCHED,
      MATCHED,
      EXPIRED,
      DEACTIVATED,
      REMOVED,
      DRAFT
    }

    public enum CommandType {
      OUTPUT,
      INPUT
    }

    public enum BasketType {
      EXCLUSIVE,
      VOLUME_CONSTRAINED,
      CUMULATIVE_VOLUME_CONSTRAINED,
      LINKED
    }

    public enum ProductStatus {
      OPEN,
      CLOSED
    }

    public enum ImportOrderStatus {
      NOT_EXECUTED,
      EXECUTED
    }

    public enum MatchedOrderStatus {
      NEW,
      VIEWED
    }

    public enum ProductDeliveryPeriod {
      MINUTES15,
      MINUTES60
    }

    public enum ActiveSubstationExecutionStatus {
      NOT_EXECUTED,
      ON_EXECUTION,
      EXECUTED,
      OFFER_SENT,
      OFFER_ACCEPTED,
      OFFER_REJECTED
    }

  }
}
