package Configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Configuration
{
    final private String key;
    private Mode mode;

    public void setMode(Mode mode) {
        this.mode = mode;
    }
}
