package FrontEnd;

/**
 * Holder class for : FrontEndInterface
 * 
 * @author OpenORB Compiler
 */
final public class FrontEndInterfaceHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal FrontEndInterface value
     */
    public FrontEnd.FrontEndInterface value;

    /**
     * Default constructor
     */
    public FrontEndInterfaceHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public FrontEndInterfaceHolder(FrontEnd.FrontEndInterface initial)
    {
        value = initial;
    }

    /**
     * Read FrontEndInterface from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = FrontEndInterfaceHelper.read(istream);
    }

    /**
     * Write FrontEndInterface into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        FrontEndInterfaceHelper.write(ostream,value);
    }

    /**
     * Return the FrontEndInterface TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return FrontEndInterfaceHelper.type();
    }

}
