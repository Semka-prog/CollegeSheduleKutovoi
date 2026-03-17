using CollegeSchedule.Data;
using CollegeSchedule.Models;
using CollegeSchedule.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
namespace CollegeSchedule.Controllers
{
    [ApiController]
    [Route("api/schedule")]
    public class ScheduleController : ControllerBase
    {
        private readonly IScheduleService _service;

        public ScheduleController(IScheduleService service)
        {
            _service = service;
        }

        // GET: api/schedule/group/ИС-12?start=2026-03-16&end=2026-03-23
        [HttpGet("group/{groupName}")]
        public async Task<IActionResult> GetSchedule(string groupName, DateTime start, DateTime end)
        {
            // Вызываем бизнес-логику из сервиса
            var result = await _service.GetScheduleForGroup(groupName, start.Date, end.Date);

            // Возвращаем результат со статусом 200 OK
            return Ok(result);
        }
    }
}