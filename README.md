# Creating a System User and Mapping to a Bundle in AEM

This guide provides step-by-step instructions for creating a system user in Adobe Experience Manager (AEM) and mapping it to a specific service user mapper.

## Prerequisites

- Access to AEM instance (typically at http://localhost:4502)
- Administrative privileges

## Steps

### 1. Create System User

You can create a system user either by defining it in your application content package or by manually creating it in the CRX Explorer.

#### Manual Creation in CRX Explorer

1. Open CRX Explorer:
[Explorer](http://localhost:4502/crx/explorer/index.jsp).


2. Navigate to **User Administration**.

3. Click on **Create system User** from the top bar.

4. Enter the required details for the system user.

5. Click on the green check box to create the system user.

### 2. Assign Necessary Permissions

1. Open the User Administration console:
2. [console](http://localhost:4502/useradmin).

3.  Find the newly created system user.

3. Assign the necessary permissions to the system user based on your application's requirements.

### 3. Define Service User Mapper

Service user mappers link your system user to a specific service. Follow these steps to define a service user mapper:

1. Open the Felix Console:
[Console] (http://localhost:4502/system/console/configMgr).


2. Find and configure the **Apache Sling Service User Mapper Service**.

3. Add a new entry mapping your service to the newly created system user.

### Example

Assume you have a service called `my-service` and a system user called `my-system-user`. The configuration would look like:

my-service=system-user=my-system-user

## Conclusion

Following these steps, you can successfully create a system user in AEM and map it to your specific service user mapper. Ensure that the permissions granted to the system user are minimal and only what is necessary for the service to function correctly.

## References

- [CRX Explorer](http://localhost:4502/crx/explorer/index.jsp)
- [User Administration](http://localhost:4502/useradmin)
- [Felix Console](http://localhost:4502/system/console/configMgr)

