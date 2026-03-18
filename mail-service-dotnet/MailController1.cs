using Microsoft.AspNetCore.Mvc;

namespace MailServer;
[ApiController]
[Route("api/mail")]
public class MailController1(HelperFunction func) : ControllerBase
{
    [HttpGet("send")]
    public ActionResult<int> SendMailAndGetCode([FromQuery]string email)
    {
        try
        {
            var code = func.SendMail(email);
            return Ok(code);
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex.Message);
            return NotFound(0);
        }
    }
}