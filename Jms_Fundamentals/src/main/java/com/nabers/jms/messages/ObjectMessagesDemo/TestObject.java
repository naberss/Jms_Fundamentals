package com.nabers.jms.messages.ObjectMessagesDemo;

import java.io.Serializable;

public class TestObject implements Serializable {

    int id;
    String Name;

    public TestObject(int id, String name) {
        this.id = id;
        Name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "TestObject{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                '}';
    }
}
