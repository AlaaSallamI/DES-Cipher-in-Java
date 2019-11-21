package sample;

import java.util.ArrayList;

public class DesCipher {

    private String C0Key, D0Key;
    private int totalRound;
    private ArrayList<String> keys = new ArrayList<>();
    int[] InitialPermutation = new int[]{1, 4, 6, 8, 3, 5, 2, 7};
    int[] FinalPermutation = new int[]{1, 7, 5, 2, 6, 3, 8, 4};
    int[] expansionTable = new int[]{4, 1, 2, 3, 2, 3, 4, 1};
    int[] S1Box = new int[]{0, 2, 1, 3, 1, 0, 3, 2, 1, 3, 2, 0, 3, 0, 2, 1};
    int[] S2Box = new int[]{1, 3, 0, 2, 0, 2, 3, 1, 0, 3, 0, 2, 2, 1, 0, 3};
    int[] permutation4Bits = new int[]{2, 1, 4, 3};
    int[] keyShift = new int[]{2, 1};

    DesCipher(int round) {
        this.totalRound = round;
    }

    public void setKeys(String C0Key, String D0key) {
        this.C0Key = C0Key;
        this.D0Key = D0key;
        generateKeys();
    }

    public String encrypt(String message) {

        message = initialPermute(message);

        int messageMid = message.length() / 2;
        String left = message.substring(0, messageMid);
        String right = message.substring(messageMid);

        for (int currentRound = 0; currentRound < totalRound; currentRound++) {
            String temp = right;
            String expandedRight = expand(right);
            String RightXorLeft = XOR(expandedRight, keys.get(currentRound));
            String ReducedRightXorLeft = reduce(RightXorLeft);
            String permuted4Bit = permute4Bits(ReducedRightXorLeft);
            right = XOR(permuted4Bit, left);
            left = temp;
        }

        return finalPermute(right + left);
    }

    public String decrypt(String encryptedMessage) {

        encryptedMessage = initialPermute(encryptedMessage);

        int messageMid = encryptedMessage.length() / 2;
        String left = encryptedMessage.substring(0, messageMid);
        String right = encryptedMessage.substring(messageMid);

        for (int currentRound = totalRound - 1; currentRound >= 0; currentRound--) {
            String temp = right;
            String expandedRight = expand(right);
            String RightXorLeft = XOR(expandedRight, keys.get(currentRound));
            String ReducedRightXorLeft = reduce(RightXorLeft);
            String permuted4Bit = permute4Bits(ReducedRightXorLeft);
            right = XOR(permuted4Bit, left);
            left = temp;
        }

        return finalPermute(right + left);
    }

    private void generateKeys() {
        String cKey = C0Key;
        String dKey = D0Key;
        for (int currentRound = 0; currentRound < totalRound; currentRound++) {
            cKey = leftRotate(cKey, keyShift[currentRound]);
            dKey = leftRotate(dKey, keyShift[currentRound]);
            keys.add(cKey + dKey);
        }
    }

    private String intToBinaryString(int num) {
        switch (num) {
            case 0:
                return "00";
            case 1:
                return "01";
            case 2:
                return "10";
            case 3:
                return "11";
            default:
                return "";
        }
    }

    private int binaryStringToNumber(String binaryString) {
        switch (binaryString) {
            case "00":
                return 0;
            case "01":
                return 1;
            case "10":
                return 2;
            case "11":
                return 3;
            default:
                return -1;
        }
    }

    private String permute(String text, int[] arr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int textIndex = 0; textIndex < arr.length; textIndex++) {
            char ch = text.charAt(arr[textIndex] - 1);
            stringBuilder.append(ch);
        }
        return stringBuilder.toString();
    }

    private String initialPermute(String text) {
        return permute(text, InitialPermutation);
    }

    private String finalPermute(String text) {
        return permute(text, FinalPermutation);
    }

    private String permute4Bits(String text) {
        return permute(text, permutation4Bits);
    }

    private String expand(String right) {
        return permute(right, expansionTable);
    }

    private String reduce(String text) {
        int messageMid = text.length() / 2;
        String left = text.substring(0, messageMid);
        String right = text.substring(messageMid);

        int S1Index = binaryStringToNumber(left.charAt(0) + "" + left.charAt(3)) * 4 + binaryStringToNumber(left.charAt(1) + "" + left.charAt(2));
        int S1Value = S1Box[S1Index];
        int S2Index = binaryStringToNumber(right.charAt(0) + "" + right.charAt(3)) * 4 + binaryStringToNumber(right.charAt(1) + "" + right.charAt(2));
        int S2Value = S2Box[S2Index];

        return intToBinaryString(S1Value) + intToBinaryString(S2Value);
    }

    private String leftRotate(String str, int d) {
        String ans = str.substring(d) + str.substring(0, d);
        return ans;
    }

    private String XOR(String left, String right) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < left.length(); i++) {
            stringBuilder.append((left.charAt(i) - '0') ^ (right.charAt(i) - '0'));
        }
        return stringBuilder.toString();
    }

}