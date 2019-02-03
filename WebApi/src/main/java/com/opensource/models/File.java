package com.opensource.models;

import com.opensource.enums.FileType;

import java.util.Date;

public class File {

    public File() {}

    public File (String _name, String _lastName, String _uniqueId, String _age, String _session) {
        this.Name = _name;
        this.LastName = _lastName;
        this.UniqueId = _uniqueId;
        this.Age = _age;
        this.Session = _session;
    }

    public File (String _name, String _lastName, String _uniqueId) {
        this.Name = _name;
        this.LastName = _lastName;
        this.UniqueId = _uniqueId;
    }

    public String Name;
    public String LastName;
    public String UniqueId;
    public String Age;
    public String Session;
}
