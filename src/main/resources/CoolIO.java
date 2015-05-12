import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CoolIO extends CoolObject {
    public CoolIO out_string(CoolString message) {
        System.out.print(message.getValue());
        return this;
    }

    public CoolIO out_int(CoolInt message) {
        System.out.print(message.getValue());
        return this;
    }

    public CoolString in_string() {
        final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        try {
            return new CoolString(input.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CoolInt in_int() {
        return new CoolInt(Integer.parseInt(this.in_string().getValue()));
    }
}
