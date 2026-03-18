namespace CollegeSchedule.DTO
{
    public class StudentGroupDto
    {
        // ID может понадобиться для будущих доработок, 
        // но для отображения названия достаточно и строки.
        // Оставим на случай, если потом захотим передавать еще и курс.
        public int GroupId { get; set; }
        public string GroupName { get; set; } = null!;

        // Опционально: можно добавить курс для сортировки или красоты
        public int Course { get; set; }
    }
}