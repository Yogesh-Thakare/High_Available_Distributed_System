package dsms;

/** 
 * Helper class for : ClinicService
 *  
 * @author OpenORB Compiler
 */ 
public class ClinicServiceHelper
{
    /**
     * Insert ClinicService into an any
     * @param a an any
     * @param t ClinicService value
     */
    public static void insert(org.omg.CORBA.Any a, dsms.ClinicService t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract ClinicService from an any
     *
     * @param a an any
     * @return the extracted ClinicService value
     */
    public static dsms.ClinicService extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return dsms.ClinicServiceHelper.narrow( a.extract_Object() );
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
     * Return the ClinicService TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "ClinicService" );
        }
        return _tc;
    }

    /**
     * Return the ClinicService IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:dsms/ClinicService:1.0";

    /**
     * Read ClinicService from a marshalled stream
     * @param istream the input stream
     * @return the readed ClinicService value
     */
    public static dsms.ClinicService read(org.omg.CORBA.portable.InputStream istream)
    {
        return(dsms.ClinicService)istream.read_Object(dsms._ClinicServiceStub.class);
    }

    /**
     * Write ClinicService into a marshalled stream
     * @param ostream the output stream
     * @param value ClinicService value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, dsms.ClinicService value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to ClinicService
     * @param obj the CORBA Object
     * @return ClinicService Object
     */
    public static ClinicService narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof ClinicService)
            return (ClinicService)obj;

        if (obj._is_a(id()))
        {
            _ClinicServiceStub stub = new _ClinicServiceStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to ClinicService
     * @param obj the CORBA Object
     * @return ClinicService Object
     */
    public static ClinicService unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof ClinicService)
            return (ClinicService)obj;

        _ClinicServiceStub stub = new _ClinicServiceStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
