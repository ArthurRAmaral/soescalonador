public class PasswordGenerator {
    public static String genereteNextPassword(Service s) {
        StringBuilder str = new StringBuilder();
        str.append(s.getKey());
        ServiceCounter.addOneAt(s);
        int num = ServiceCounter.getCoutOF(s);
        if (num < 10) str.append("00").append(num);
        else if (num < 100) str.append("0").append(num);
        else str.append(num);
        return str.toString();
    }

}
