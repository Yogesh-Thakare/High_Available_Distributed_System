package FrontEnd;

/** 
 * Helper class for : FrontEndInterface
 *  
 * @author OpenORB Compiler
 */ 
public class FrontEndInterfaceHelper
{
    /**
     * Insert FrontEndInterface into an any
     * @param a an any
     * @param t FrontEndInterface value
     */
    public static void insert(org.omg.CORBA.Any a, FrontEnd.FrontEndInterface t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract FrontEndInterface from an any
     *
     * @param a an any
     * @return the extracted FrontEndInterface value
     */
    public static FrontEnd.FrontEndInterface extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return FrontEnd.FrontEndInterfaceHelper.narrow( a.extract_Object() );
        }
        catch ( final org.omg.CORBA.BAD_PARAM e )
        {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the FrontEndInterface TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "FrontEndInterface" );
        }
        return _tc;
    }

    /**
     * Return the FrontEndInterface IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:FrontEnd/FrontEndInterface:1.0";

    /**
     * Read FrontEndInterface from a marshalled stream
     * @param istream the input stream
     * @return the readed FrontEndInterface value
     */
    public static FrontEnd.FrontEndInterface read(org.omg.CORBA.portable.InputStream istream)
    {
        return(FrontEnd.FrontEndInterface)istream.read_Object(FrontEnd._FrontEndInterfaceStub.class);
    }

    /**
     * Write FrontEndInterface into a marshalled stream
     * @param ostream the output stream
     * @param value FrontEndInterface value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, FrontEnd.FrontEndInterface value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to FrontEndInterface
     * @param obj the CORBA Object
     * @return FrontEndInterface Object
     */
    public static FrontEndInterface narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof FrontEndInterface)
            return (FrontEndInterface)obj;

        if (obj._is_a(id()))
        {
            _FrontEndInterfaceStub stub = new _FrontEndInterfaceStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to FrontEndInterface
     * @param obj the CORBA Object
     * @return FrontEndInterface Object
     */
    public static FrontEndInterface unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof FrontEndInterface)
            return (FrontEndInterface)obj;

        _FrontEndInterfaceStub stub = new _FrontEndInterfaceStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
