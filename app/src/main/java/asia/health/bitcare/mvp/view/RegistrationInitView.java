package asia.health.bitcare.mvp.view;

/**
 * Created by An Pham on 06-Feb-17.
 * Last modifined on 06-Feb-17
 */

public interface RegistrationInitView {

    void onRegistrationInitSuccess(String serviceMsg);

    void onRegistrationInitFail(String errorMessage);
}
