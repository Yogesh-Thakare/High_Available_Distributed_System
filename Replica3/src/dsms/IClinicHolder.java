package dsms;

/**
 * Holder class for : IClinic
 * 
 * @author OpenORB Compiler
 */
final public class IClinicHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal IClinic value
     */
    public dsms.IClinic value;

    /**
     * Default constructor
     */
    public IClinicHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public IClinicHolder(dsms.IClinic initial)
    {
        value = initial;
    }

    /**
     * Read IClinic from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = IClinicHelper.read(istream);
    }

    /**
     * Write IClinic into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        IClinicHelper.write(ostream,value);
    }

    /**
     * Return the IClinic TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return IClinicHelper.type();
    }

}
