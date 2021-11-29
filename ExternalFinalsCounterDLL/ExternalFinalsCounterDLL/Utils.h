#pragma once

#include "pch.h"

class Utils
{
public:
	static void messageBox(const char* message)
	{
		MessageBoxA(nullptr, message, "ExternalFinalsCounter", MB_OK);
	}
};