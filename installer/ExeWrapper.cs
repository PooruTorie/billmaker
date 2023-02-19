using System;

namespace BillMaker
{
    class Program
    {
        static void Main(string[] args)
        {
            System.Diagnostics.Process process = new System.Diagnostics.Process();
            System.Diagnostics.ProcessStartInfo startInfo = new System.Diagnostics.ProcessStartInfo();
            startInfo.WindowStyle = System.Diagnostics.ProcessWindowStyle.Hidden;
            startInfo.FileName = "java.exe";
            startInfo.Arguments = "-jar BillMaker.jar";
            process.StartInfo = startInfo;
            process.Start();
        }
    }
}
