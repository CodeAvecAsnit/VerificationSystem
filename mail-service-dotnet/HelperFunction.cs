using System.Net;
using MailKit.Security;

namespace MailServer;
using MimeKit;
using MailKit.Net.Smtp;

public class HelperFunction(Random random)
{
    private int GenerateRandomCode()
    {
        return random.Next(100000, 999999);
    }

    public int SendMail(string eMail)
    {
        try
        {
            var code = GenerateRandomCode();

            var email = new MimeMessage();
            email.From.Add(MailboxAddress.Parse("furnituremandu@gmail.com"));
            email.To.Add(MailboxAddress.Parse(eMail));
            email.Subject = "Verification of Your Sign Up";
            email.Body = new TextPart(MimeKit.Text.TextFormat.Plain)
            {
                Text = $"Your code is: {code}. Please do not share it with anyone."
            };

            using var smtp = new MailKit.Net.Smtp.SmtpClient();
            smtp.Connect("smtp.gmail.com", 587, MailKit.Security.SecureSocketOptions.StartTls);
            smtp.Authenticate("yourmailhere@gmail.com", "appPassworhere); 
            smtp.Send(email);
            smtp.Disconnect(true);

            return code;
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Email sending failed: {ex.Message}");
            return -1;
        }
    }
}