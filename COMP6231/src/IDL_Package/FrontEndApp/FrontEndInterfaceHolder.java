package FrontEndApp;

/**
* FrontEndApp/FrontEndInterfaceHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from FrontEndInterface.idl
* Friday, August 5, 2016 10:33:16 o'clock AM EDT
*/

public final class FrontEndInterfaceHolder implements org.omg.CORBA.portable.Streamable
{
  public FrontEndApp.FrontEndInterface value = null;

  public FrontEndInterfaceHolder ()
  {
  }

  public FrontEndInterfaceHolder (FrontEndApp.FrontEndInterface initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = FrontEndApp.FrontEndInterfaceHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    FrontEndApp.FrontEndInterfaceHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return FrontEndApp.FrontEndInterfaceHelper.type ();
  }

}
