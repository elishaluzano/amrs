import java.io.*;
import java.util.*;

public class Register{
      String registerName;
      Integer registerValue;
      boolean busy; 

      public Register(String name, Integer value)
      {
            this.registerName = name;
            this.registerValue = value;
            this.busy = false;
      }

      public Integer getRegValue(){
            return this.registerValue;
      }

      public void setRegValue(Integer value){
            this.registerValue = value;
      }

      public boolean isBusy(){
            return this.busy;
      }

      public void setBusy(boolean busy){
            this.busy = busy;
      }

      
}
