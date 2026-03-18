using CollegeSchedule.DTO;
using CollegeSchedule.Services;
using Microsoft.AspNetCore.Mvc;

namespace CollegeSchedule.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class GroupsController : ControllerBase
    {
        private readonly IScheduleService _service;

        public GroupsController(IScheduleService service)
        {
            _service = service;
        }

        /// <summary>
        /// GET /api/groups
        /// Возвращает список всех групп для выпадающего списка
        /// </summary>
        [HttpGet]
        public async Task<ActionResult<List<StudentGroupDto>>> GetAllGroups()
        {
            var groups = await _service.GetAllGroupsAsync();
            return Ok(groups);
        }
    }
}