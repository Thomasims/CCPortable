package CCPortable.API;

import dan200.computer.api.IComputerAccess;

public interface IPDACommandHandler {
    public String[] getHandledCommands();

    public Object[] handleCommand(IComputerAccess computer, int method, Object[] arg) throws Exception;
}