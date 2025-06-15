import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import React from "react";

interface FormInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  value: string | number;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  containerClassName?: string;
}

const FormInput: React.FC<FormInputProps> = ({
  label,
  value,
  onChange,
  containerClassName = "",
  ...inputProps
}) => (
  <div className={containerClassName}>
    <Label>{label}</Label>
    <Input value={value} onChange={onChange} {...inputProps} />
  </div>
);

export default FormInput;
