using System;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using AndroidPostVideoToServer.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace AndroidPostVideoToServer.Controllers
{

    [Route("api/file")]
    public class MyServerController : ControllerBase
    {


        [HttpGet]
        public ActionResult<String> asd(){
            System.Console.WriteLine("GET METODU CALİST");
            return Ok("asdsadfsafasdf");
        }

        [AcceptVerbs]
        [HttpPost]
        public async Task<ActionResult<User>> Upload(IFormFile photo,string description){
            
            System.Console.WriteLine("worked!!! worked!!!");
            try
            {

                /*
                    IFormFileCollection files = Request.Form.Files;
                    files.ToList().ForEach(file =>
                    {
                        if (file.Length > 0){

                        
                            var extention = Path.GetExtension(file.FileName);
                            var randomName = string.Format($"{Guid.NewGuid()}{extention}");
                    
                            using(FileStream stream = new FileStream(Path.Combine(Directory.GetCurrentDirectory(),"wwwroot/Uploads",randomName),FileMode.Create)){
                                file.CopyTo(stream);
                            }
                        }

                    });

                return Ok();
*/
                if(photo != null){
                    var extention = Path.GetExtension(photo.FileName);
                    var randomName = string.Format($"{Guid.NewGuid()}{extention}");
                    var path = Path.Combine(Directory.GetCurrentDirectory(),"wwwroot/Uploads",randomName);

                    using(var stream = new FileStream(path,FileMode.Create)){
                        await photo.CopyToAsync(stream);
                    }

                }
                System.Console.WriteLine("file: " + photo.FileName);

                return Ok();



            }catch(Exception ex)
            {
                    throw ex;
            }
            if(photo != null){
                var extention = Path.GetExtension(photo.FileName);
                var randomName = string.Format($"{Guid.NewGuid()}{extention}");
                var path = Path.Combine(Directory.GetCurrentDirectory(),"wwwroot/Uploads",randomName);
                using(var stream = new FileStream(path,FileMode.Create)){
                    await photo.CopyToAsync(stream);
                }
                System.Console.WriteLine("description: " + description);
                System.Console.WriteLine("file: : " + photo.FileName);

            }

            return Ok(new User(){id = 1,name = "işlem başarılı dostum sunucuya geldi dosya dı: " + photo.FileName});
        }
        
    }
}