using AndroidPostVideoToServer.Models;
using Microsoft.AspNetCore.Mvc;

namespace AndroidPostVideoToServer.Controllers
{

    [Route("api/test")]
    public class TestController : ControllerBase
    {
        
        [HttpGet]
        public ActionResult<User> getUser(){
            
            var user = new User(){id = 1,name = "Halil Kaya"};
            System.Console.WriteLine(user.name);
            return Ok(user);

        }

        [HttpPost]
        public ActionResult post(int id,string name){
            System.Console.WriteLine("id: " + id);
            System.Console.WriteLine("name: " + name);
            return Ok(new User(){id = id,name = name});
        }



    }
}