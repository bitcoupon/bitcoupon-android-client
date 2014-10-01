package bitcoupon;

import java.util.ArrayList;
import java.util.List;

public class Main {

  private static final String GENERATE_CREATION_TRANSACTION = "generateCreationTransaction";
  private static final String GENERATE_SEND_TRANSACTION = "generateSendTransaction";
  private static final String GET_CREATOR_PUBLIC_KEYS = "getCreatorPublicKeys";
  private static final String VERIFY_TRANSACTION = "verifyTransaction";

  public static void main(String[] args) {
//    evaluateMethod(args);

    testTransaction();
  }

  private static void testTransaction() {
    Transaction trans = BitCoupon.generateCreationTransaction("5Kf9gd8faKhhq9jZTsNhq2MtViHA1dWdhRg9k4ovszTKz5DCeBT");
    String json = Transaction.toJson(trans);
    Transaction fromJson = Transaction.fromJson(json);
    System.out.println(json);

    Transaction trans2 = BitCoupon.generateCreationTransaction("5Kf9gd8faKhhq9jZTsNhq2MtViHA1dWdhRg9k4ovszTKz5DCeBT");
    Transaction trans3 = BitCoupon.generateCreationTransaction("5Kf9gd8faKhhq9jZTsNhq2MtViHA1dWdhRg9k4ovszTKz5DCeBT");
    Transaction trans4 = BitCoupon.generateCreationTransaction("5Kf9gd8faKhhq9jZTsNhq2MtViHA1dWdhRg9k4ovszTKz5DCeBT");

    List<Transaction> transactionList = new ArrayList<Transaction>();
    transactionList.add(trans2);
    transactionList.add(trans3);
    transactionList.add(trans4);

    String jsonTransactionList = TransactionList.toJson(transactionList);
    System.out.println(jsonTransactionList);

    List<Transaction> listFromJson = TransactionList.fromJson(jsonTransactionList).getList();
  }

  private static void evaluateMethod(String[] args) {
    String methodName = args[0];

    checkArgumentsLength(methodName, args.length);

    if (methodName.equalsIgnoreCase(GENERATE_CREATION_TRANSACTION)) {
      generateCreationTransaction(args[1]);
    } else if (methodName.equalsIgnoreCase(GENERATE_SEND_TRANSACTION)) {
      generateSendTransaction(args[1], args[2], args[3], args[4]);
    } else if (methodName.equalsIgnoreCase(GET_CREATOR_PUBLIC_KEYS)) {
      getCreatorPublicKeys(args[1]);
    } else if (methodName.equalsIgnoreCase(VERIFY_TRANSACTION)) {
      verifyTransaction(args[1], args[2]);
    }
  }

  private static void checkArgumentsLength(String methodName, int argsLength) {
    boolean correct = checkLength(methodName, argsLength);
    if (! correct) {
      String message = "Wrong number of arguments to method: " + methodName;

      // TODO Choose between exception or exit with failure.
      //throw new IllegalArgumentException("Wrong number of arguments to method: " + methodName);

      System.err.println("Wrong number of arguments to method: " + methodName);
      System.exit(1);
    }
  }

  private static boolean checkLength(String methodName, int argsLength) {
    int length = 0;
    if (methodName.equalsIgnoreCase(GENERATE_CREATION_TRANSACTION)) {
      length = 2;
    } else if (methodName.equalsIgnoreCase(GENERATE_SEND_TRANSACTION)) {
      length = 5;
    } else if (methodName.equalsIgnoreCase(GET_CREATOR_PUBLIC_KEYS)) {
      length = 2;
    } else if (methodName.equalsIgnoreCase(VERIFY_TRANSACTION)) {
      length = 3;
    }
    return length == argsLength;
  }

  private static void verifyTransaction(String transactionJson, String transactionHistoryJson) {
    Transaction transaction = Transaction.fromJson(transactionJson);
    List<Transaction> transactionHistory = TransactionList.fromJson(transactionHistoryJson).getList();
    BitCoupon.verifyTransaction(transaction, transactionHistory);
  }

  private static void getCreatorPublicKeys(String transactionHistoryJson) {
    List<Transaction> transactionHistory = TransactionList.fromJson(transactionHistoryJson).getList();
    BitCoupon.getCreatorAddresses(transactionHistory);
  }

  private static void generateSendTransaction(String privateKey, String creatorPublicKey, String transactionHistoryJson,
                                              String receiverAddress) {
    List<Transaction> transactionHistory = TransactionList.fromJson(transactionHistoryJson).getList();
    BitCoupon.generateSendTransaction(privateKey, creatorPublicKey, receiverAddress, transactionHistory);
  }

  private static void generateCreationTransaction(String privateKey) {
    BitCoupon.generateCreationTransaction(privateKey);
  }
}
