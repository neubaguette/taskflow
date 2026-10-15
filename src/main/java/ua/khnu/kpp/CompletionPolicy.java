
package ua.khnu.kpp;

public interface CompletionPolicy {

    boolean allows(int units, int defects, boolean approved);

}
