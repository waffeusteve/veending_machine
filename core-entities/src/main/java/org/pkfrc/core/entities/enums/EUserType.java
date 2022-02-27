package org.pkfrc.core.entities.enums;

public enum EUserType {
    Buyer, seller, Root;

    @Override
    public String toString() {
        return "EUserType." +  this.name() ;
    }

}
