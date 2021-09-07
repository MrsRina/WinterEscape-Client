package club.cpacket.solaros.api.value;

/**
 * @author SrRina
 * @since 06/09/2021 at 20:38
 **/
public class ValueGeneric <T> extends Value {
    public static final int GENERIC = 99;

    private T value;

    public ValueGeneric(String tag, String description, T generic) {
        super(tag, description, GENERIC);

        this.setValue(generic);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
