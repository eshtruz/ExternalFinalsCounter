#include "pch.h"
#include "ExternalFinalsCounter.h"

DWORD WINAPI inject(LPVOID hModule)
{
    ExternalFinalsCounter(reinterpret_cast<HMODULE>(hModule));

    return 0;
}

BOOL APIENTRY DllMain(HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved)
{
    switch (ul_reason_for_call)
    {
    case DLL_PROCESS_ATTACH:
        if (!CreateThread(nullptr, 0, inject, hModule, 0, nullptr))
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
