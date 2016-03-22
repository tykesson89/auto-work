package UserPackage;

import java.io.Serializable;

/**
 * Created by Henrik on 2016-03-17.
 */
public interface UserInterface extends Serializable {

    public String getFirstname();
    public void setFirstname(String firstname);
    public String getLastname();
    public void setLastname(String lastname);
    public String getEmail();
    public void setEmail(String email);
    public String getPassword();
    public void setPassword(String password);
    public int getUserid();
    public void setUserid(int userid);
    public String toString();
    public Object[] toObjectArray();




}
