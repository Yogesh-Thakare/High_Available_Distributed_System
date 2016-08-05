package dsms;

/** 
 * Helper class for : IClinic
 *  
 * @author OpenORB Compiler
 */ 
public class IClinicHelper
{
    /**
     * Insert IClinic into an any
     * @param a an any
     * @param t IClinic value
     */
    public static void insert(org.omg.CORBA.Any a, dsms.IClinic t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract IClinic from an any
     *
     * @param a an any
     * @return the extracted IClinic value
     */
    public static dsms.IClinic extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return dsms.IClinicHelper.narrow( a.extract_Object() );
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
     * Return the IClinic TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "IClinic" );
        }
        return _tc;
    }

    /**
     * Return the IClinic IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:dsms/IClinic:1.0";

    /**
     * Read IClinic from a marshalled stream
     * @param istream the input stream
     * @return the readed IClinic value
     */
    public static dsms.IClinic read(org.omg.CORBA.portable.InputStream istream)
    {
        return(dsms.IClinic)istream.read_Object(dsms._IClinicStub.class);
    }

    /**
     * Write IClinic into a marshalled stream
     * @param ostream the output stream
     * @param value IClinic value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, dsms.IClinic value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to IClinic
     * @param obj the CORBA Object
     * @return IClinic Object
     */
    public static IClinic narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof IClinic)
            return (IClinic)obj;

        if (obj._is_a(id()))
        {
            _IClinicStub stub = new _IClinicStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to IClinic
     * @param obj the CORBA Object
     * @return IClinic Object
     */
    public static IClinic unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof IClinic)
            return (IClinic)obj;

        _IClinicStub stub = new _IClinicStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
