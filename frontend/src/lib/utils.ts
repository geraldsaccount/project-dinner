import { clsx, type ClassValue } from "clsx";
import type { ReactNode } from "react";
import { twMerge } from "tailwind-merge";

export type GridLayoutProps = {
  children: ReactNode;
  className?: string;
  gap?: string;
  gridCols: Partial<{
    base: number;
    sm: number;
    md: number;
    lg: number;
    xl: number;
    "2xl"?: number;
  }>;
};

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export const buildGridCols = (gridCols?: GridLayoutProps["gridCols"]) => {
  if (!gridCols) {
    return "grid-cols-1";
  }
  return Object.entries(gridCols)
    .map(([breakpoint, cols]) =>
      breakpoint === "base"
        ? `grid-cols-${cols}`
        : `${breakpoint}:grid-cols-${cols}`
    )
    .join(" ");
};
