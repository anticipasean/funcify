package funcify.testing;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;
import java.util.Optional;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-09-06
 */
@AllArgsConstructor(staticName = "of")
public class NonZeroExitStatusValidator extends SecurityManager {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<SecurityManager> parentSecurityManager;

    @Override
    public Object getSecurityContext() {
        return parentSecurityManager.map(SecurityManager::getSecurityContext)
                                    .orElseGet(super::getSecurityContext);
    }

    @Override
    public void checkPermission(final Permission perm) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkPermission(perm);
        });
    }

    @Override
    public void checkPermission(final Permission perm,
                                final Object context) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkPermission(perm,
                                context);
        });
    }

    @Override
    public void checkCreateClassLoader() {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkCreateClassLoader();
        });
    }

    @Override
    public void checkAccess(final Thread t) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkAccess(t);
        });
    }

    @Override
    public void checkAccess(final ThreadGroup g) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkAccess(g);
        });
    }

    @Override
    public void checkExit(final int status) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkExit(status);
        });
        if (status != 0) {
            throw new AssertionError(String.format("process exited with a non-zero status: [ status: %d ]",
                                                   status));
        }
    }

    @Override
    public void checkExec(final String cmd) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkExec(cmd);
        });
    }

    @Override
    public void checkLink(final String lib) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkLink(lib);
        });
    }

    @Override
    public void checkRead(final FileDescriptor fd) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkRead(fd);
        });
    }

    @Override
    public void checkRead(final String file) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkRead(file);
        });
    }

    @Override
    public void checkRead(final String file,
                          final Object context) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkRead(file,
                          context);
        });
    }

    @Override
    public void checkWrite(final FileDescriptor fd) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkWrite(fd);
        });
    }

    @Override
    public void checkWrite(final String file) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkWrite(file);
        });
    }

    @Override
    public void checkDelete(final String file) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkDelete(file);
        });
    }

    @Override
    public void checkConnect(final String host,
                             final int port) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkConnect(host,
                             port);
        });
    }

    @Override
    public void checkConnect(final String host,
                             final int port,
                             final Object context) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkConnect(host,
                             port,
                             context);
        });
    }

    @Override
    public void checkListen(final int port) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkListen(port);
        });
    }

    @Override
    public void checkAccept(final String host,
                            final int port) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkAccept(host,
                            port);
        });
    }

    @Override
    public void checkMulticast(final InetAddress maddr) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkMulticast(maddr);
        });
    }


    @Override
    public void checkPropertiesAccess() {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkPropertiesAccess();
        });
    }

    @Override
    public void checkPropertyAccess(final String key) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkPropertyAccess(key);
        });
    }

    @Override
    public void checkPrintJobAccess() {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkPrintJobAccess();
        });
    }

    @Override
    public void checkPackageAccess(final String pkg) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkPackageAccess(pkg);
        });
    }

    @Override
    public void checkPackageDefinition(final String pkg) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkPackageDefinition(pkg);
        });
    }

    @Override
    public void checkSetFactory() {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkSetFactory();
        });
    }


    @Override
    public void checkSecurityAccess(final String target) {
        parentSecurityManager.ifPresent(psm -> {
            psm.checkSecurityAccess(target);
        });
    }

    @Override
    public ThreadGroup getThreadGroup() {
        return parentSecurityManager.map(SecurityManager::getThreadGroup)
                                    .orElseGet(super::getThreadGroup);
    }
}
