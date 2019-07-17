package jinlo.gum.core.runtime;

import java.util.ArrayList;
import java.util.List;

public class ExtensionExecuteDetail<R,E> {

    public static class FacadeDetail<E> {
        private String facadeCode;

        private E result;

        public String getFacadeCode() {
            return facadeCode;
        }

        public void setFacadeCode(String facadeCode) {
            this.facadeCode = facadeCode;
        }

        public E getResult() {
            return result;
        }

        public void setResult(E result) {
            this.result = result;
        }
    }

    private String extensionCode;

    private List<FacadeDetail<E>> details=new ArrayList<>();

    private R result;

    public ExtensionExecuteDetail(String extensionCode) {
        this.extensionCode = extensionCode;
    }

    public String getExtensionCode() {
        return extensionCode;
    }

    public List<FacadeDetail<E>> getDetails() {
        return details;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }
}
