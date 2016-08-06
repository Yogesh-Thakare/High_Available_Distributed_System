package FrontEnd;

/**
 * Interface definition: FrontEndInterface.
 * 
 * @author OpenORB Compiler
 */
public class _FrontEndInterfaceStub extends org.omg.CORBA.portable.ObjectImpl
        implements FrontEndInterface
{
    static final String[] _ids_list =
    {
        "IDL:FrontEnd/FrontEndInterface:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = FrontEnd.FrontEndInterfaceOperations.class;

    /**
     * Operation exectuteOperation
     */
    public String exectuteOperation(String method, String managerID, String firstName, String lastName, String address, String phone, String specialization, String location, String designation, String status, String statusDate, String recordID, String fieldName, String newValue, String remoteClinicServerName)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("exectuteOperation",true);
                    _output.write_string(method);
                    _output.write_string(managerID);
                    _output.write_string(firstName);
                    _output.write_string(lastName);
                    _output.write_string(address);
                    _output.write_string(phone);
                    _output.write_string(specialization);
                    _output.write_string(location);
                    _output.write_string(designation);
                    _output.write_string(status);
                    _output.write_string(statusDate);
                    _output.write_string(recordID);
                    _output.write_string(fieldName);
                    _output.write_string(newValue);
                    _output.write_string(remoteClinicServerName);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exectuteOperation",_opsClass);
                if (_so == null)
                   continue;
                FrontEnd.FrontEndInterfaceOperations _self = (FrontEnd.FrontEndInterfaceOperations) _so.servant;
                try
                {
                    return _self.exectuteOperation( method,  managerID,  firstName,  lastName,  address,  phone,  specialization,  location,  designation,  status,  statusDate,  recordID,  fieldName,  newValue,  remoteClinicServerName);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
