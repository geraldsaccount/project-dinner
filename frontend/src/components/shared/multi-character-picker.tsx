import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { Checkbox } from "@/components/ui/checkbox";
import { Button } from "@/components/ui/button";
import { Character } from "@/types/creation";
import React from "react";

interface MultiCharacterPickerProps {
  characters: Character[];
  selectedIds: string[];
  onChange: (ids: string[]) => void;
  placeholder?: string;
  buttonClassName?: string;
  disabled?: boolean;
}

const MultiCharacterPicker: React.FC<MultiCharacterPickerProps> = ({
  characters,
  selectedIds,
  onChange,
  placeholder = "Select characters",
  buttonClassName = "w-[220px] justify-start",
  disabled = false,
}) => {
  const toggle = (id: string) => {
    if (selectedIds.includes(id)) {
      onChange(selectedIds.filter((cid) => cid !== id));
    } else {
      onChange([...selectedIds, id]);
    }
  };

  return (
    <Popover>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          className={buttonClassName}
          disabled={disabled}
        >
          {selectedIds.length === 0
            ? placeholder
            : characters
                .filter((c) => selectedIds.includes(c.id))
                .map((c) => c.name)
                .join(", ")}
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-[220px] p-2">
        {characters.length > 0 ? (
          <div className="flex flex-col gap-1 max-h-60 overflow-y-auto">
            {characters.map((c) => (
              <label
                key={c.id}
                className="flex items-center gap-2 cursor-pointer px-2 py-1 rounded hover:bg-muted"
              >
                <Checkbox
                  checked={selectedIds.includes(c.id)}
                  onCheckedChange={() => toggle(c.id)}
                />
                <span>{c.name}</span>
              </label>
            ))}
          </div>
        ) : (
          <span className="text-muted-foreground text-sm">
            Add characters first
          </span>
        )}
      </PopoverContent>
    </Popover>
  );
};

export default MultiCharacterPicker;
