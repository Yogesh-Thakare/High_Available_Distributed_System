package FrontEndApp;


/**
* FrontEndApp/FrontEndInterfacePOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from FrontEndInterface.idl
* Friday, August 5, 2016 10:33:16 o'clock AM EDT
*/

public abstract class FrontEndInterfacePOA extends org.omg.PortableServer.Servant
 implements FrontEndApp.FrontEndInterfaceOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("exectuteOperation", new java.lang.Integer (0));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // FrontEndApp/FrontEndInterface/exectuteOperation
       {
         String method = in.read_string ();
         String managerID = in.read_string ();
         String firstName = in.read_string ();
         String lastName = in.read_string ();
         String address = in.read_string ();
         String phone = in.read_string ();
         String specialization = in.read_string ();
         String location = in.read_string ();
         String designation = in.read_string ();
         String status = in.read_string ();
         String statusDate = in.read_string ();
         String recordID = in.read_string ();
         String fieldName = in.read_string ();
         String newValue = in.read_string ();
         String remoteClinicServerName = in.read_string ();
         String $result = null;
         $result = this.exectuteOperation (method, managerID, firstName, lastName, address, phone, specialization, location, designation, status, statusDate, recordID, fieldName, newValue, remoteClinicServerName);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:FrontEndApp/FrontEndInterface:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public FrontEndInterface _this() 
  {
    return FrontEndInterfaceHelper.narrow(
    super._this_object());
  }

  public FrontEndInterface _this(org.omg.CORBA.ORB orb) 
  {
    return FrontEndInterfaceHelper.narrow(
    super._this_object(orb));
  }


} // class FrontEndInterfacePOA
