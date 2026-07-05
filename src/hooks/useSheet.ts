"use client";
import { useState, useEffect, useCallback, useRef } from "react";
import { fetchSheet } from "@/lib/api";

export type FetchState = "idle" | "loading" | "success" | "error";
export interface UseSheetResult<T> {
  data: T[];
  state: FetchState;
  isLoading: boolean;
  isError: boolean;
  refetch: () => void;
}

// Module-level shared cache — persists across component mounts in same session
const _cache: Map<string, { data: unknown[]; ts: number }> = new Map();
const CACHE_TTL = 30_000; // 30 s

function getCached<T>(key: string): T[] | null {
  const entry = _cache.get(key);
  if (entry && Date.now() - entry.ts < CACHE_TTL) return entry.data as T[];
  return null;
}

export function useSheet<T>(sheetName: string, enabled = true): UseSheetResult<T> {
  const cached = getCached<T>(sheetName);
  const [data,  setData]  = useState<T[]>(cached ?? []);
  const [state, setState] = useState<FetchState>(cached ? "success" : "idle");
  const mountedRef = useRef(true);

  useEffect(() => {
    mountedRef.current = true;
    return () => { mountedRef.current = false; };
  }, []);

  const fetch_ = useCallback(async (bust = false) => {
    if (!sheetName) return;
    if (!bust) {
      const hit = getCached<T>(sheetName);
      if (hit) { if (mountedRef.current) { setData(hit); setState("success"); } return; }
    }
    if (mountedRef.current) setState(s => s === "success" ? "success" : "loading");

    for (let attempt = 0; attempt < 3; attempt++) {
      try {
        const result = await Promise.race<T[]>([
          fetchSheet<T>(sheetName),
          new Promise<T[]>((_, rej) => setTimeout(() => rej(new Error("timeout")), 12_000)),
        ]);
        const arr = Array.isArray(result) ? result : [];
        _cache.set(sheetName, { data: arr, ts: Date.now() });
        if (mountedRef.current) { setData(arr); setState("success"); }
        return;
      } catch {
        if (attempt < 2) await new Promise(r => setTimeout(r, 700 * (attempt + 1)));
      }
    }
    if (mountedRef.current && state !== "success") setState("error");
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [sheetName]);

  const refetch = useCallback(() => { _cache.delete(sheetName); fetch_(true); }, [sheetName, fetch_]);
  useEffect(() => { if (enabled) fetch_(); }, [fetch_, enabled]);

  return {
    data,
    state,
    isLoading: (state === "loading" || state === "idle") && data.length === 0,
    isError:   state === "error" && data.length === 0,
    refetch,
  };
}
