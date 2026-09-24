using System;
using System.Threading;
using System.Threading.Tasks;
using WorkOrderManagement.Domain.Enums;
using WorkOrderManagement.Application.Commands;

namespace WorkOrderManagement.Application.Commands;

public class UpdateWorkOrderStatusCommand
{
    public Guid WorkOrderId { get; set; }
    public WorkOrderStatus NewStatus { get; set; }
}

public class UpdateWorkOrderStatusCommandHandler
{
    private readonly IWorkOrderRepository _repository;

    public UpdateWorkOrderStatusCommandHandler(IWorkOrderRepository repository)
    {
        _repository = repository;
    }

    public async Task<bool> Handle(UpdateWorkOrderStatusCommand request, CancellationToken cancellationToken)
    {
        // Mocking the get and update since we don't have a real DB in this lab
        // In reality, this would fetch from repo, update, and save
        return true;
    }
}
