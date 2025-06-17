import SectionHeader from "@/components/shared/section-header";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import FormInput from "@/components/ui/form-input";
import FormTextarea from "@/components/ui/form-text-area";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Separator } from "@/components/ui/separator";
import { Character, CharacterStageInfo, Stage } from "@/types/creation";
import { Trash2 } from "lucide-react";
import { useEffect, useRef, useState } from "react";
import { CharacterStageCard } from "./character-stage-card";

type Props = {
  character: Character;
  updateCharacter: (id: string, updatedCharacter: Character) => void;
  deleteCharacter: (id: string) => void;
  allCharacters: Character[];
  globalStages: Stage[];
};

const CharacterForm = ({
  character,
  updateCharacter,
  deleteCharacter,
  allCharacters,
  globalStages,
}: Props) => {
  const [avatarPreview, setAvatarPreview] = useState(
    character.avatarUrl ||
      "https://placehold.co/256x256/e2e8f0/64748b?text=Avatar+Preview"
  );
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  useEffect(() => {
    setAvatarPreview(
      character.avatarUrl ||
        "https://placehold.co/256x256/e2e8f0/64748b?text=Avatar+Preview"
    );
  }, [character.avatarUrl]);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const previewUrl = URL.createObjectURL(file);
      setAvatarPreview(previewUrl);
      updateCharacter(character.id, { ...character, avatarUrl: previewUrl });
    }
  };

  const handleFieldUpdate = (
    field: keyof Omit<Character, "stageInfo" | "relationships">,
    value: string | number | boolean
  ) => {
    updateCharacter(character.id, { ...character, [field]: value });
  };

  const handleRelationshipUpdate = (otherCharId: string, value: string) => {
    const newRelationships = {
      ...character.relationships,
      [otherCharId]: value,
    };
    updateCharacter(character.id, {
      ...character,
      relationships: newRelationships,
    });
  };

  const setGender = (gender: "Female" | "Male" | "Other") => {
    handleFieldUpdate("gender", gender);
  };

  const otherCharacters = allCharacters.filter((c) => c.id !== character.id);

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-start">
        <SectionHeader title={character.name} />
        <Button
          variant="destructive"
          size="icon"
          onClick={() => deleteCharacter(character.id)}
        >
          <Trash2 />
        </Button>
      </div>
      <div>
        <div className="flex flex-col md:flex-row gap-8">
          <div className="flex-1 space-y-4">
            <FormInput
              label="Name"
              value={character.name}
              onChange={(e) => handleFieldUpdate("name", e.target.value)}
            />
            <FormInput
              label="Role"
              value={character.role}
              onChange={(e) => handleFieldUpdate("role", e.target.value)}
            />
            <div className="flex gap-4">
              <div className="flex-1">
                <Label>Age</Label>
                <Input
                  type="number"
                  inputMode="numeric"
                  pattern="[0-9]*"
                  min={0}
                  value={character.age}
                  onBeforeInput={(e) => {
                    if (!/\d/.test(e.data ?? "")) {
                      e.preventDefault();
                    }
                  }}
                  onChange={(e) => {
                    const sanitized = e.target.value.replace(/[^\d]/g, "");
                    handleFieldUpdate(
                      "age",
                      sanitized === "" ? "" : parseInt(sanitized, 10)
                    );
                  }}
                />
              </div>
              <div className="flex-1">
                <Label>Gender</Label>
                <Select value={character.gender} onValueChange={setGender}>
                  <SelectTrigger className="w-full">
                    <SelectValue placeholder="Select a gender" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="Female">Female</SelectItem>
                    <SelectItem value="Male">Male</SelectItem>
                    <SelectItem value="Other">Other</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div className="flex items-center mt-6">
                <Checkbox
                  id={`primary-${character.id}`}
                  checked={character.isPrimary}
                  onCheckedChange={(checked) =>
                    handleFieldUpdate("isPrimary", !!checked)
                  }
                  className="mr-2"
                />
                <Label htmlFor={`primary-${character.id}`}>
                  Primary Character
                </Label>
              </div>
            </div>
          </div>
          <div className="flex flex-col items-center md:w-64 w-full mt-8 md:mt-0">
            <button
              className="cursor-pointer h-48 w-48 flex items-center justify-center"
              style={{ aspectRatio: 1 }}
              onClick={() =>
                fileInputRef.current && fileInputRef.current.click()
              }
            >
              <img
                className="h-full w-full object-cover rounded-full"
                src={avatarPreview}
                alt="Avatar Preview"
              />
            </button>
            <input
              type="file"
              accept="image/*"
              onChange={handleFileChange}
              className="hidden"
              ref={fileInputRef}
            />
          </div>
        </div>
      </div>
      <FormTextarea
        label="Shop Description"
        value={character.shopDescription}
        onChange={(e) => handleFieldUpdate("shopDescription", e.target.value)}
      />
      <FormTextarea
        label="Private Description"
        value={character.privateDescription}
        onChange={(e) =>
          handleFieldUpdate("privateDescription", e.target.value)
        }
      />

      <Separator />
      <div className="space-y-4 pt-4">
        <h4 className="text-lg font-semibold">Relationships</h4>
        {otherCharacters.length > 0 ? (
          otherCharacters.map((other) => (
            <FormTextarea
              key={other.id}
              label={`Relationship to ${other.name}`}
              value={character.relationships[other.id] || ""}
              onChange={(e) =>
                handleRelationshipUpdate(other.id, e.target.value)
              }
            />
          ))
        ) : (
          <p className="text-sm text-slate-500">
            Add other characters to define relationships.
          </p>
        )}
      </div>
      <Separator />

      <div className="space-y-4 pt-4">
        <div className="flex justify-between items-center">
          <h4 className="text-lg font-semibold">Private Stage Information</h4>
        </div>
        <div className="space-y-4">
          {globalStages.map((gStage) => {
            const stageInfo = character.stageInfo.find(
              (si: CharacterStageInfo) => si.stageId === gStage.id
            );
            if (!stageInfo) return null;
            return (
              <CharacterStageCard
                key={gStage.id}
                character={character}
                stage={gStage}
              />
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default CharacterForm;
