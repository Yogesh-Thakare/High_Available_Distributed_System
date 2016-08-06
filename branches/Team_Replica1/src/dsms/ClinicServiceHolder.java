package dsms;

/**
 * Holder class for : ClinicService
 * 
 * @author OpenORB Compiler
 */
final public class ClinicServiceHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal ClinicService value
     */
    public dsms.ClinicService value;

    /**
     * Default constructor
     */
    public ClinicServiceHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public ClinicServiceHolder(dsms.ClinicService initial)
    {
        value = initial;
    }

    /**
     * Read ClinicService from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = ClinicServiceHelper.read(istream);
    }

    /**
     * Write ClinicService into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        ClinicServiceHelper.write(ostream,value);
    }

    /**
     * Return the ClinicService TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return ClinicServiceHelper.type();
    }

}
