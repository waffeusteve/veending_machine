package org.pkfrc.core.entities.enums;

public enum EUserType {
    buyer, seller;

    @Override
    public String toString() {
        return "EUserType." +  this.name() ;
    }

}
