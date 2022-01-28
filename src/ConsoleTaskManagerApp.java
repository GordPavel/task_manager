import java.util.Map;

import task.TaskConnectionsManager;
import task.TaskManager;
import task.TaskManagerFacade;
import task.TaskStatusManager;
import task.TaskTypesManager;
import task.implementations.DefaultTaskStatusManager;
import task.implementations.EpicTasksStatusManager;
import task.implementations.HardCodedTaskTypesManager;
import task.implementations.SimpleTaskConnectionsManager;
import task.implementations.SimpleTaskManager;
import task.models.TaskType;
import view.ConsoleTasksViewNegotiator;
import view.TasksViewNegotiator;

import static task.TaskTypesManager.EPIC_TYPE;

public class ConsoleTaskManagerApp {

    private static final TaskManager taskManager = new SimpleTaskManager();
    private static final TaskConnectionsManager connectionsManager = new SimpleTaskConnectionsManager();
    private static final TaskStatusManager defaultStatusManager = new DefaultTaskStatusManager();
    private static final Map<TaskType, TaskStatusManager> specificStatusManagers = Map.of(
            EPIC_TYPE, new EpicTasksStatusManager(connectionsManager)
    );
    private static final TaskTypesManager typesManager = new HardCodedTaskTypesManager();
    private static final TaskManagerFacade managerFacade = new TaskManagerFacade(
            taskManager,
            typesManager,
            defaultStatusManager,
            specificStatusManagers,
            connectionsManager
    );

    private static final TasksViewNegotiator viewNegotiator = new ConsoleTasksViewNegotiator(managerFacade);

    public static void main(String[] args) {
        viewNegotiator.greeting();
        while (true) {
            try {
                viewNegotiator.showMenu();
                viewNegotiator.performAction();
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Простите, произошла непредвиденная ошибка. Завершаю программу.");
                break;
            }
        }
    }
}
