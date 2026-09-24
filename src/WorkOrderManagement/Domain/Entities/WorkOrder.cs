using System;
using WorkOrderManagement.Domain.Enums;

namespace WorkOrderManagement.Domain.Entities;

public class WorkOrder
{
    public Guid Id { get; private set; }
    public Guid EquipmentId { get; private set; }
    public Priority Priority { get; private set; }
    public string Description { get; private set; }
    public Guid? TechnicianId { get; private set; }
    public WorkOrderStatus Status { get; private set; }
    public DateTime CreatedDate { get; private set; }
    public string CreatedBy { get; private set; }
    public DateTime? ModifiedDate { get; private set; }
    public string ModifiedBy { get; private set; }
    public byte[] RowVersion { get; private set; }

    // Private constructor for EF Core
    private WorkOrder() { }

    public static WorkOrder Create(Guid equipmentId, Priority priority, string description, string createdBy)
    {
        if (string.IsNullOrWhiteSpace(description))
            throw new ArgumentException("Description cannot be empty.", nameof(description));

        if (description.Length > 2000)
            throw new ArgumentException("Description cannot exceed 2000 characters.", nameof(description));

        var workOrder = new WorkOrder
        {
            Id = Guid.NewGuid(),
            EquipmentId = equipmentId,
            Priority = priority,
            Description = description,
            Status = WorkOrderStatus.Open,
            CreatedDate = DateTime.UtcNow,
            CreatedBy = createdBy
        };

        // TODO: Add Domain Event (e.g. WorkOrderCreatedDomainEvent)
        return workOrder;
    }
}
