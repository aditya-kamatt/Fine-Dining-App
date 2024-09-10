package com.jadd.easyrestro.classes;

public class Employee extends User {

    String ownerUid;
    long id;
    boolean assigned;

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }


    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Employee(){

    }

    public Employee(User user,long id) {
        super(user.getName(), user.getEmail(), user.getPhone());
        this.id = id;
        assigned = false;
    }

}
