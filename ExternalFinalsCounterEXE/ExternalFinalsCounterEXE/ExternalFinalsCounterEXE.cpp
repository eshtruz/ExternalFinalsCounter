#include "pch.h"

static DWORD pid = 0;

BOOL CALLBACK EnumWindowsProc(HWND hWnd, LPARAM lParam)
{
	const int length = GetWindowTextLengthA(hWnd);

	if (length == 0)
	{
		return TRUE;
	}

	char* windowTitle = new char[length + 1];
	if (GetWindowTextA(hWnd, windowTitle, length + 1) == 0)
	{
		delete[] windowTitle;

		return TRUE;
	}

	if (strstr(windowTitle, "Minecraft 1.8.9")
		|| strstr(windowTitle, "Lunar Client (1.8.9"))
	{
		GetWindowThreadProcessId(hWnd, &pid);

		delete[] windowTitle;

		return FALSE;
	}

	delete[] windowTitle;

    return TRUE;
}

bool getPid()
{
	if (EnumWindows(EnumWindowsProc, 0) == 0 && pid == 0)
	{
		std::cout << "Failed to enumerate windows" << std::endl;
		return false;
	}

	if (pid == 0)
	{
		std::cout << "No Minecraft found" << std::endl;
		return false;
	}

	return true;
}

bool inject(const char* dll)
{
	HANDLE hProcess = OpenProcess(PROCESS_CREATE_THREAD | PROCESS_VM_OPERATION | PROCESS_VM_WRITE, FALSE, pid);

	if (!hProcess)
	{
		std::cout << "Failed to open process" << std::endl;
		return false;
	}

	HMODULE kernel32DLL = GetModuleHandle(L"kernel32.dll");

	if (!kernel32DLL)
	{
		std::cout << "Failed to get kernel32.dll" << std::endl;
		CloseHandle(hProcess);
		return false;
	}

	LPVOID loadLibraryAProcAddress = GetProcAddress(kernel32DLL, "LoadLibraryA");

	if (!loadLibraryAProcAddress)
	{
		std::cout << "Failed to get LoadLibraryA function" << std::endl;
		CloseHandle(hProcess);
		return false;
	}

	LPVOID pDLL = VirtualAllocEx(hProcess, 0, strlen(dll), MEM_COMMIT, PAGE_READWRITE);

	if (!pDLL)
	{
		std::cout << "Failed to allocate memory" << std::endl;
		CloseHandle(hProcess);
		return false;
	}

	if (WriteProcessMemory(hProcess, pDLL, dll, strlen(dll), nullptr) == 0)
	{
		std::cout << "Failed to write memory" << std::endl;
		VirtualFreeEx(hProcess, pDLL, 0, MEM_RELEASE);
		CloseHandle(hProcess);
		return false;
	}

	HANDLE hThread = CreateRemoteThread(hProcess, nullptr, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(loadLibraryAProcAddress), pDLL, 0, nullptr);

	if (!hThread)
	{
		std::cout << "Failed to create thread" << std::endl;
		VirtualFreeEx(hProcess, pDLL, 0, MEM_RELEASE);
		CloseHandle(hProcess);
		return false;
	}

	if (WaitForSingleObject(hThread, INFINITE) == WAIT_FAILED)
	{
		std::cout << "Failed to wait for thread" << std::endl;
		VirtualFreeEx(hProcess, pDLL, 0, MEM_RELEASE);
		CloseHandle(hThread);
		CloseHandle(hProcess);
		return false;
	}

	DWORD exitCode;
	if (GetExitCodeThread(hThread, &exitCode) == 0)
	{
		std::cout << "Failed to get thread exit code" << std::endl;
		VirtualFreeEx(hProcess, pDLL, 0, MEM_RELEASE);
		CloseHandle(hThread);
		CloseHandle(hProcess);
		return false;
	}

	if (!exitCode)
	{
		std::cout << "Failed to load library" << std::endl;
		VirtualFreeEx(hProcess, pDLL, 0, MEM_RELEASE);
		CloseHandle(hThread);
		CloseHandle(hProcess);
		return false;
	}

	VirtualFreeEx(hProcess, pDLL, 0, MEM_RELEASE);

	CloseHandle(hThread);

	CloseHandle(hProcess);

	return true;
}

int main()
{
	std::cout << "Attempting to inject" << std::endl;

	const char* dll = "ExternalFinalsCounterDLL.dll";
	const char* jar = "ExternalFinalsCounterJAR.jar";
	const char* jarDLL = "ExternalFinalsCounterJARDLL.dll";

	if (GetFileAttributesA(dll) == INVALID_FILE_ATTRIBUTES)
	{
		std::cout << "ExternalFinalsCounterDLL.dll not found in the current folder" << std::endl;
		system("pause");
		return 0;
	}

	if (GetFileAttributesA(jar) == INVALID_FILE_ATTRIBUTES)
	{
		std::cout << "ExternalFinalsCounterJAR.jar not found in the current folder" << std::endl;
		system("pause");
		return 0;
	}

	if (GetFileAttributesA(jarDLL) == INVALID_FILE_ATTRIBUTES)
	{
		std::cout << "ExternalFinalsCounterJARDLL.dll not found in the current folder" << std::endl;
		system("pause");
		return 0;
	}

	char fullDLL[MAX_PATH];

	if (GetFullPathNameA(dll, MAX_PATH, fullDLL, nullptr) == 0)
	{
		std::cout << "Failed to get full DLL path" << std::endl;
		system("pause");
		return 0;
	}

	if (!getPid())
	{
		system("pause");
		return 0;
	}

	if (!inject(fullDLL))
	{
		system("pause");
		return 0;
	}

	std::cout << "Stage 1 complete" << std::endl;
	system("pause");
}
