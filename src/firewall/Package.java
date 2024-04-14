package firewall;

import shared.Message;

import java.io.Serializable;

public record Package(Integer sender, Integer receiver, Message content) implements Serializable {
}
