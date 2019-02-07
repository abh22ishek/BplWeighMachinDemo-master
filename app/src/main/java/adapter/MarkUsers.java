package adapter;

import java.util.*;

import model.*;

public interface MarkUsers {
    void markAll();
    void unMarkAll();

    void activeUsers(List<String> activeUsers);
    void dormantUsers(List<String> dormantUsers);
}
