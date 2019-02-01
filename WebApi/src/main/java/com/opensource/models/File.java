package com.opensource.models;

import com.opensource.enums.FileType;

import java.util.Date;

public class File {

    public File() {}

    public File (String _name, FileType _type, String _owner, Date _creationDate, Date _updateDate) {
        this.Name = _name;
        this.Type = _type;
        this.Owner = _owner;
        this.CreationDate = _creationDate;
        this.UpdateDate = _updateDate;
    }

    public File (String _name, FileType _type, String _owner) {
        this.Name = _name;
        this.Type = _type;
        this.Owner = _owner;
        this.CreationDate = new Date();
        this.UpdateDate = new Date();
    }

    public String Name;
    public FileType Type;
    public Date CreationDate;
    public Date UpdateDate;
    public String Owner;
}
