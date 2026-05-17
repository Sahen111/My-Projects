using Microsoft.AspNetCore.Mvc;

namespace DecorBusiness.Controllers
{
    public class HomeController : Controller
    {
        public IActionResult Index()
        {
            return View();
        }
    }
}
