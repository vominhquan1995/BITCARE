package asia.health.bitcare.mvp.view;

/**
 * Created by An Pham on 06-Feb-17.
 * Last modifined on 06-Feb-17
 */

public interface InvestigateView {
    void onSuccess(int fitAge, String serviceMsg);

    void onError(String errorMessage);
}
