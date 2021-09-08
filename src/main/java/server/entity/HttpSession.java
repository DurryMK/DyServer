package server.entity;

import java.util.Set;

public interface HttpSession {

    Object getAttribute(String name);

    void setAttribute(String name, Object value);

    Set<String> getNames();

    void removeAttribute(String name);
}
