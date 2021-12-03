#include "pch.h"
#include "ExternalFinalsCounter.h"

void inject()
{
    ExternalFinalsCounter();
}

BOOL APIENTRY DllMain(HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved)
{
    switch (ul_reason_for_call)
    {
    case DLL_PROCESS_ATTACH:
        if (!CreateThread(nullptr, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(inject), nullptr, 0, nullptr))
        {
            Utils::messageBox("Failed to create thread");
            return FALSE;
        }
        break;
    case DLL_THREAD_ATTACH:
    case DLL_THREAD_DETACH:
    case DLL_PROCESS_DETACH:
        break;
    }
    return TRUE;
}
