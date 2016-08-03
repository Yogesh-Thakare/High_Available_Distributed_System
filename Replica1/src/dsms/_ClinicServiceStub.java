package dsms;

/**
 * Interface definition: ClinicService.
 * 
 * @author OpenORB Compiler
 */
public class _ClinicServiceStub extends org.omg.CORBA.portable.ObjectImpl
        implements ClinicService
{
    static final String[] _ids_list =
    {
        "IDL:dsms/ClinicService:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = dsms.ClinicServiceOperations.class;

    /**
     * Operation createDRecord
     */
    public String createDRecord(String firstName, String lastName, String address, String phone, String specialization, String location, String managerID)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("createDRecord",true);
                    _output.write_string(firstName);
                    _output.write_string(lastName);
                    _output.write_string(address);
                    _output.write_string(phone);
                    _output.write_string(specialization);
                    _output.write_string(location);
                    _output.write_string(managerID);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("createDRecord",_opsClass);
                if (_so == null)
                   continue;
                dsms.ClinicServiceOperations _self = (dsms.ClinicServiceOperations) _so.servant;
                try
                {
                    return _self.createDRecord( firstName,  lastName,  address,  phone,  specialization,  location,  managerID);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation createNRecord
     */
    public String createNRecord(String firstName, String lastName, String designation, String status, String statusDate, String managerID)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("createNRecord",true);
                    _output.write_string(firstName);
                    _output.write_string(lastName);
                    _output.write_string(designation);
                    _output.write_string(status);
                    _output.write_string(statusDate);
                    _output.write_string(managerID);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("createNRecord",_opsClass);
                if (_so == null)
                   continue;
                dsms.ClinicServiceOperations _self = (dsms.ClinicServiceOperations) _so.servant;
                try
                {
                    return _self.createNRecord( firstName,  lastName,  designation,  status,  statusDate,  managerID);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getRecordCounts
     */
    public String getRecordCounts(String recordType, String managerID)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getRecordCounts",true);
                    _output.write_string(recordType);
                    _output.write_string(managerID);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getRecordCounts",_opsClass);
                if (_so == null)
                   continue;
                dsms.ClinicServiceOperations _self = (dsms.ClinicServiceOperations) _so.servant;
                try
                {
                    return _self.getRecordCounts( recordType,  managerID);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation editRecord
     */
    public String editRecord(String recordID, String fieldName, String newValue, String managerID)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("editRecord",true);
                    _output.write_string(recordID);
                    _output.write_string(fieldName);
                    _output.write_string(newValue);
                    _output.write_string(managerID);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("editRecord",_opsClass);
                if (_so == null)
                   continue;
                dsms.ClinicServiceOperations _self = (dsms.ClinicServiceOperations) _so.servant;
                try
                {
                    return _self.editRecord( recordID,  fieldName,  newValue,  managerID);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation transferRecord
     */
    public String transferRecord(String recordID, String remoteClinicServerName, String managerID)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("transferRecord",true);
                    _output.write_string(recordID);
                    _output.write_string(remoteClinicServerName);
                    _output.write_string(managerID);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("transferRecord",_opsClass);
                if (_so == null)
                   continue;
                dsms.ClinicServiceOperations _self = (dsms.ClinicServiceOperations) _so.servant;
                try
                {
                    return _self.transferRecord( recordID,  remoteClinicServerName,  managerID);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
